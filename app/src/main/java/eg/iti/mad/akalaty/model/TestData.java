package eg.iti.mad.akalaty.model;

import java.io.Serializable;

public class TestData implements Serializable {
    private int num;
    private String name;

    public TestData(int num, String name) {
        this.num = num;
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TestData{" +
                "num=" + num +
                ", name='" + name + '\'' +
                '}';
    }
}
