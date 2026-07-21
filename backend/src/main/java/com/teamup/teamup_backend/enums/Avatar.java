package com.teamup.teamup_backend.enums;
public enum Avatar {
    DEFAULT("avatar01.png"),
    AVATAR_01("avatar01.png"),
    AVATAR_02("avatar02.png"),
    AVATAR_03("avatar03.png"),
    AVATAR_04("avatar04.png"),
    AVATAR_05("avatar05.png"),
    AVATAR_06("avatar06.png"),
    AVATAR_07("avatar07.png"),
    AVATAR_08("avatar08.png"),
    AVATAR_09("avatar09.png"),
    AVATAR_10("avatar10.png");

    private final String fileName;

    Avatar(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}