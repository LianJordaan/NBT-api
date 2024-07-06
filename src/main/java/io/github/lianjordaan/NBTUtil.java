package io.github.lianjordaan;

import com.sk89q.jnbt.*;
import com.google.gson.*;

import java.util.*;

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

    public static CompoundTag fromJsonString(String jsonString) {
        JsonElement jsonElement = JsonParser.parseString(jsonString);
        if (jsonElement.isJsonObject()) {
            return parseCompoundTag(jsonElement.getAsJsonObject());
        }
        throw new IllegalArgumentException("Invalid JSON string for NBT data");
    }

    private static CompoundTag parseCompoundTag(JsonObject jsonObject) {
        Map<String, Tag> tagMap = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            tagMap.put(entry.getKey(), parseTag(entry.getValue()));
        }
        return new CompoundTag(tagMap);
    }

    private static Tag parseTag(JsonElement jsonElement) {
        if (jsonElement.isJsonObject()) {
            return parseCompoundTag(jsonElement.getAsJsonObject());
        } else if (jsonElement.isJsonArray()) {
            return parseListTag(jsonElement.getAsJsonArray());
        } else if (jsonElement.isJsonPrimitive()) {
            JsonPrimitive primitive = jsonElement.getAsJsonPrimitive();
            if (primitive.isBoolean()) {
                return new ByteTag((byte) (primitive.getAsBoolean() ? 1 : 0));
            } else if (primitive.isNumber()) {
                Number number = primitive.getAsNumber();
                if (number instanceof Byte) {
                    return new ByteTag(number.byteValue());
                } else if (number instanceof Short) {
                    return new ShortTag(number.shortValue());
                } else if (number instanceof Integer) {
                    return new IntTag(number.intValue());
                } else if (number instanceof Long) {
                    return new LongTag(number.longValue());
                } else if (number instanceof Float) {
                    return new FloatTag(number.floatValue());
                } else if (number instanceof Double) {
                    return new DoubleTag(number.doubleValue());
                }
            } else if (primitive.isString()) {
                return new StringTag(primitive.getAsString());
            }
        }
        throw new IllegalArgumentException("Unsupported JSON element type for NBT data");
    }

    private static ListTag parseListTag(JsonArray jsonArray) {
        List<Tag> tagList = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            tagList.add(parseTag(element));
        }
        // Determine the type of the tags in the list
        Class<? extends Tag> tagType = tagList.isEmpty() ? EndTag.class : tagList.get(0).getClass();
        return new ListTag(tagType, tagList);
    }

}
