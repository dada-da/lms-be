package org.com.lms_be.feature.quiz_attempt;

import org.com.lms_be.exception.ResourceNotFoundException;
import org.com.lms_be.feature.answer.AnswerEntity;
import org.com.lms_be.feature.answer.AnswerRepository;
import org.com.lms_be.feature.enrollment.EnrollmentRepository;
import org.com.lms_be.feature.lesson.LessonEntity;
import org.com.lms_be.feature.lesson_progress.LessonProgressService;
import org.com.lms_be.feature.question.QuestionEntity;
import org.com.lms_be.feature.question.QuestionRepository;
import org.com.lms_be.feature.quiz.QuizEntity;
import org.com.lms_be.feature.quiz.QuizService;
import org.com.lms_be.feature.user.UserEntity;
import org.com.lms_be.feature.user.UserRepository;
import org.com.lms_be.util.PublishStatus;
import org.com.lms_be.util.QuizAttempt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class QuizAttemptService {

    private final QuizAttemptRepository quizAttemptRepository;
    private final QuizService quizService;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonProgressService lessonProgressService;

    public QuizAttemptService(QuizAttemptRepository quizAttemptRepository,
                              QuizService quizService,
                              QuestionRepository questionRepository,
                              AnswerRepository answerRepository,
                              UserRepository userRepository,
                              EnrollmentRepository enrollmentRepository,
                              LessonProgressService lessonProgressService) {
        this.quizAttemptRepository = quizAttemptRepository;
        this.quizService = quizService;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.lessonProgressService = lessonProgressService;
    }

    @Transactional
    public QuizAttemptResponseDTO submit(QuizAttemptSubmitDTO request, Long studentId) {
        UserEntity student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("User", studentId));

        QuizEntity quiz = quizService.getById(request.getQuizId());
        LessonEntity lesson = quiz.getLesson();

        if (lesson.getStatus() != PublishStatus.PUBLISHED) {
            throw new IllegalStateException("Cannot submit attempt for a quiz on an unpublished lesson");
        }

        Long courseId = lesson.getCourse().getId();
        if (!enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), courseId)) {
            throw new IllegalStateException("Student is not enrolled in the course this quiz belongs to");
        }

        List<QuestionEntity> questions = questionRepository.findAllByQuizIdOrderBySequenceAsc(quiz.getId());
        if (questions.isEmpty()) {
            throw new IllegalStateException("Quiz has no questions");
        }

        Map<Long, Set<Long>> correctByQuestion = loadCorrectAnswerIds(questions);
        Map<Long, Set<Long>> selectedByQuestion = request.getAnswers().stream()
                .collect(Collectors.toMap(
                        QuizAnswerSubmissionDTO::getQuestionId,
                        a -> a.getSelectedAnswerIds() != null ? new HashSet<>(a.getSelectedAnswerIds()) : new HashSet<>(),
                        (a, b) -> a));

        int correctCount = 0;
        for (QuestionEntity q : questions) {
            Set<Long> correct = correctByQuestion.getOrDefault(q.getId(), Set.of());
            Set<Long> selected = selectedByQuestion.getOrDefault(q.getId(), Set.of());
            if (!correct.isEmpty() && correct.equals(selected)) {
                correctCount++;
            }
        }

        int score = (int) Math.round(100.0 * correctCount / questions.size());
        QuizAttempt status = score >= quiz.getPassingScore() ? QuizAttempt.PASS : QuizAttempt.FAIL;

        QuizAttemptEntity attempt = new QuizAttemptEntity();
        attempt.setStudent(student);
        attempt.setQuiz(quiz);
        attempt.setScore(score);
        attempt.setStatus(status);
        QuizAttemptEntity saved = quizAttemptRepository.save(attempt);

        if (status == QuizAttempt.PASS) {
            lessonProgressService.markCompletedFromQuiz(student.getId(), lesson);
        }

        return QuizAttemptResponseDTO.from(saved);
    }

    public List<QuizAttemptResponseDTO> getMyAttemptsForQuiz(Long studentId, Long quizId) {
        return quizAttemptRepository.findAllByStudentIdAndQuizIdOrderByAttemptAtDesc(studentId, quizId).stream()
                .map(QuizAttemptResponseDTO::from)
                .toList();
    }

    private Map<Long, Set<Long>> loadCorrectAnswerIds(List<QuestionEntity> questions) {
        List<Long> questionIds = questions.stream().map(QuestionEntity::getId).toList();
        List<AnswerEntity> answers = answerRepository.findAllByQuestionIdIn(questionIds);
        return answers.stream()
                .filter(AnswerEntity::isCorrect)
                .collect(Collectors.groupingBy(
                        a -> a.getQuestion().getId(),
                        Collectors.mapping(AnswerEntity::getId, Collectors.toSet())));
    }
}
