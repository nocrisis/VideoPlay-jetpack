package com.catherine.libnetwork;

public class TestPrimitive {
    public static void main(String[] args) throws Exception {
        System.out.println((char) 65);
        System.out.println(isWrapClass(Long.class));
        System.out.println(isWrapClass(Integer.class));
        System.out.println(isWrapClass(String.class));
        System.out.println(isWrapClass(TestPrimitive.class));
        System.out.println(int.class.isPrimitive());
        System.out.println(boolean.class.isPrimitive());
        System.out.println(Void.class.isPrimitive());
    }

    public static boolean isWrapClass(Class clz) {
        try {
            return ((Class) clz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }
}
