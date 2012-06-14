package org.holodeck.security.service;

/**
 * This is used only for testing
 *
 * @author Hamid Ben Malek
 */
public class SecureService
{
  public int add(int a, int b)
  {
    System.out.println("====== SecureService is being invoked");
    return a+b;
  }
}