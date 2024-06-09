package org.lt.project.service;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;

public class LogListenerAdapter implements TailerListener {

    @Override
    public void fileNotFound() {
    }

    @Override
    public void fileRotated() {
    }

    @Override
    public void handle(Exception arg0) {
    }

    @Override
    public void handle(String arg0) {
    }

    @Override
    public void init(Tailer arg0) {
    }

    
}