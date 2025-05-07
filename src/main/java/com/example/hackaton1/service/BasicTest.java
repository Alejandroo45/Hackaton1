package com.example.hackaton1.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BasicTest {

    @Test
    public void testBasicFunctionality() {
        String testString = "Sparky AI System";


        String upperCaseString = testString.toUpperCase();

        assertEquals("SPARKY AI SYSTEM", upperCaseString);
        assertTrue(testString.contains("AI"));
    }

    @Test
    public void testBasicMath() {
        int a = 5;
        int b = 10;

        int sum = a + b;

        assertEquals(15, sum);
    }
}