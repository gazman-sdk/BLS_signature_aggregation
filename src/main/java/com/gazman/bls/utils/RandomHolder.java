package com.gazman.bls.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by Ilya Gazman on 2/3/2018.
 */
public class RandomHolder {
    public static final SecureRandom RANDOM;
    static {
        SecureRandom random;
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            random = new SecureRandom();
            e.printStackTrace();
        }
        RANDOM = random;
    }
}
