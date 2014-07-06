/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sml.test.model;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PropertyGenerator {
    private final Random randomGenerator;
    private final int widthLimit;
    private final int depthLimit;
    private final int currentId;
    private static final int GEN_ID_LIMIT = 20;
    private static final int PROP_ID_LIMIT = 1 << 23;
    private static final int VAL_ID_LIMIT = 1 << 23;
    public static final String GEN_NAME_PREFIX = "gen_prop";
    public static final String GEN_VAL_PREFIX = "gen_val";
    
    public PropertyGenerator(int width, int depthLimit) {
        this.randomGenerator = ThreadLocalRandom.current();
        this.widthLimit = width;
        this.depthLimit = depthLimit;
        this.currentId = Math.abs(randomGenerator.nextInt(GEN_ID_LIMIT));
    }
        
    private String getPropertyName() {
        return String.format("%s_%02d_%08d_%d%d", GEN_NAME_PREFIX, currentId, Math.abs(randomGenerator.nextInt(PROP_ID_LIMIT)), widthLimit, depthLimit);
    }

    private String getPropertyVal() {
        return String.format("%s_%02d_%08d_%d%d", GEN_VAL_PREFIX, currentId, Math.abs(randomGenerator.nextInt(VAL_ID_LIMIT)), widthLimit, depthLimit);
    }

    public Root getRoot() {
        Root root = new Root();
        generateChildren(root, nextZeroOrPositive(widthLimit), nextZeroOrPositive(depthLimit));
        return root;
    }

    public Property getProperty() {
        return generateProperty(nextZeroOrPositive(widthLimit), nextZeroOrPositive(depthLimit));
    }
    
    private int nextZeroOrPositive(int n) {
        if (n > 0) {
            int next = randomGenerator.nextInt(n);
            return next > 0 ? next : 0;
        }
        else {
            return 0;
        }
    }
    
    private Property generateProperty(int width, int depth) {
        Property result = new Property();
        result.setName(getPropertyName());
        result.setVal(getPropertyVal());
        if (depth > 0 && width > 0) {
            generateChildren(result, width, depth);
        }
        return result;
    }
    
    private void generateChildren(Property parent, int width, int depth) {
        int amount = width;
        while (amount-- > 0) {
            Property child = generateProperty(nextZeroOrPositive(width), nextZeroOrPositive(depth - 1));
            child.setParent(parent);
            parent.getChildren().add(child);
        }
    }

    private void generateChildren(Root root, int width, int depth) {
        int amount = width;
        while (amount-- > 0) {
            Property child = generateProperty(nextZeroOrPositive(width), nextZeroOrPositive(depth - 1));
            root.getProperties().add(child);
        }
    }

 }
