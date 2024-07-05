package io.github.lianjordaan;

import com.sk89q.jnbt.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class NBTUtil {

    public static String toJsonString(Tag tag) {
        if (tag instanceof CompoundTag) {
            CompoundTag compoundTag = (CompoundTag) tag;
            StringBuilder json = new StringBuilder("{");

            Set<Map.Entry<String, Tag>> entries = compoundTag.getValue().entrySet();
            for (Map.Entry<String, Tag> entry : entries) {
                json.append("\"").append(entry.getKey()).append("\":").append(toJsonString(entry.getValue())).append(",");
            }
            if (json.charAt(json.length() - 1) == ',') {
                json.deleteCharAt(json.length() - 1);
            }
            json.append("}");
            return json.toString();
        } else if (tag instanceof ListTag) {
            ListTag listTag = (ListTag) tag;
            StringBuilder json = new StringBuilder("[");
            for (Tag element : listTag.getValue()) {
                json.append(toJsonString(element)).append(",");
            }
            if (json.charAt(json.length() - 1) == ',') {
                json.deleteCharAt(json.length() - 1);
            }
            json.append("]");
            return json.toString();
        } else if (tag instanceof ByteTag) {
            return String.valueOf(((ByteTag) tag).getValue());
        } else if (tag instanceof ShortTag) {
            return String.valueOf(((ShortTag) tag).getValue());
        } else if (tag instanceof IntTag) {
            return String.valueOf(((IntTag) tag).getValue());
        } else if (tag instanceof LongTag) {
            return String.valueOf(((LongTag) tag).getValue());
        } else if (tag instanceof FloatTag) {
            return String.valueOf(((FloatTag) tag).getValue());
        } else if (tag instanceof DoubleTag) {
            return String.valueOf(((DoubleTag) tag).getValue());
        } else if (tag instanceof ByteArrayTag) {
            return Arrays.toString(((ByteArrayTag) tag).getValue());
        } else if (tag instanceof StringTag) {
            return "\"" + ((StringTag) tag).getValue() + "\"";
        } else if (tag instanceof IntArrayTag) {
            return Arrays.toString(((IntArrayTag) tag).getValue());
        } else if (tag instanceof LongArrayTag) {
            return Arrays.toString(((LongArrayTag) tag).getValue());
        }
        return "";
    }
}
