package org.com.lms_be.util;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class VideoContentValidator {

    private static final Pattern DISALLOWED_TAG =
            Pattern.compile("(?i)<\\s*(video|source|iframe|embed|object)\\b");

    private static final Pattern URL_ATTR =
            Pattern.compile("(?i)\\b(?:href|src)\\s*=\\s*[\"']([^\"']+)[\"']");

    private static final Pattern VIDEO_EXTENSION =
            Pattern.compile("(?i)\\.(mp4|webm|mov|m4v|avi|mkv|ogv|ogg|flv|wmv|3gp|m3u8|mpd|ts)(?:[?#].*)?$");

    private static final Set<String> VIDEO_HOSTS = Set.of(
            "youtube.com", "youtu.be", "m.youtube.com", "youtube-nocookie.com",
            "vimeo.com", "player.vimeo.com",
            "dailymotion.com", "dai.ly",
            "twitch.tv", "clips.twitch.tv",
            "tiktok.com", "vm.tiktok.com"
    );

    public void rejectVideoTags(String rawHtml) {
        if (rawHtml == null) {
            return;
        }
        if (DISALLOWED_TAG.matcher(rawHtml).find()) {
            throw new IllegalArgumentException(
                    "Video or embed tags (<video>, <iframe>, <embed>, <object>, <source>) are not allowed in lesson content");
        }
    }

    public void rejectVideoUrls(String html) {
        if (html == null) {
            return;
        }
        Matcher m = URL_ATTR.matcher(html);
        while (m.find()) {
            String url = m.group(1).trim();
            if (looksLikeVideo(url)) {
                throw new IllegalArgumentException(
                        "Video links are not allowed in lesson content: " + url);
            }
        }
    }

    private boolean looksLikeVideo(String url) {
        String lower = url.toLowerCase();
        if (VIDEO_EXTENSION.matcher(lower).find()) {
            return true;
        }
        for (String host : VIDEO_HOSTS) {
            if (lower.contains(host)) {
                return true;
            }
        }
        return false;
    }
}
