package com.airxiao.audio.bean;

import java.io.Serializable;

public class FileInfo implements Serializable {

    private String id;
    private int duration;
    private String path;
    private String name;
    private long size;
    private FileType dateType;
    private FileInfo videoPreviewInfo;

    public enum FileType {
        Video,
        Pic,
        Voice
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String fileName) {
        this.name = fileName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public FileType getDateType() {
        return dateType;
    }

    public void setDateType(FileType dateType) {
        this.dateType = dateType;
        if (dateType == FileType.Video) {
            videoPreviewInfo = new FileInfo();
            videoPreviewInfo.setDateType(FileType.Pic);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FileInfo getVideoPreviewInfo() {
        return videoPreviewInfo;
    }

}
