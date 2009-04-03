package com.infomancers.collections.yield.asm;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbd
 * Date: Jul 20, 2007
 * Time: 1:51:49 AM
 * To change this template use File | Settings | File Templates.
 */
public final class NewMember {
    private String name;
    private int index;
    private TypeDescriptor type;

    public NewMember(int index) {
        this.index = index;
        this.name = "slot$" + index;
    }

    public NewMember(int index, TypeDescriptor type) {
        this(index);

        mergeType(type);
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public String getDesc() {
        return type.getDesc();
    }

    public void mergeType(TypeDescriptor curType) {
        this.type = this.type == null || this.type == curType ? curType : TypeDescriptor.Object;
    }

    public TypeDescriptor getType() {
        return type;
    }

    public String toString() {
        return "NewMember: [name: " + name + ", index: " + type + ", type: " + type + "]";
    }
}
