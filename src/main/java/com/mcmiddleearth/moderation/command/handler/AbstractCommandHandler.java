package com.mcmiddleearth.moderation.command.handler;

public class AbstractCommandHandler {

    private String command;

    public AbstractCommandHandler(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}