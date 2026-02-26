package com.example.Task.Service.business.Impl;

public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(String msg) {
    super(msg);
  }
}

