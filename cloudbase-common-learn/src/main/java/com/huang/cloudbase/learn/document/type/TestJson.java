package com.huang.cloudbase.learn.document.type;

import java.util.List;

public class TestJson {
    private List<Video> videos;

    static class Video {
        private String title;

        private String link;

        private String description;

        private String video_cover_url;

        private String pubDate;

        private double duration;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVideo_cover_url() {
            return video_cover_url;
        }

        public void setVideo_cover_url(String video_cover_url) {
            this.video_cover_url = video_cover_url;
        }

        public String getPubDate() {
            return pubDate;
        }

        public void setPubDate(String pubDate) {
            this.pubDate = pubDate;
        }

        public double getDuration() {
            return duration;
        }

        public void setDuration(double duration) {
            this.duration = duration;
        }
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}
