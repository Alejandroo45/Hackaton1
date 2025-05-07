package com.example.hackaton1.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BasicTest {

    @Test
    public void testBasicFunctionality() {
        // Arrange
        String testString = "Sparky AI System";

        // Act
        String upperCaseString = testString.toUpperCase();

        // Assert
        assertEquals("SPARKY AI SYSTEM", upperCaseString);
        assertTrue(testString.contains("AI"));
    }

    @Test
    public void testBasicMath() {
        // Arrange
        int a = 5;
        int b = 10;

        // Act
        int sum = a + b;

        // Assert
        assertEquals(15, sum);
    }
}