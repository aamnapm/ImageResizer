package ir.aamnapm.resize_image.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum EResizeMethod {

    L_FIT("lfit"),
    M_FIT("mfit");

    private static final Map<String, EResizeMethod> BY_NAME = new HashMap<>();

    static {
        for (EResizeMethod e : values()) {
            BY_NAME.put(e.methodName, e);
        }
    }

    public String methodName;

    public static EResizeMethod valueOfMethodName(String methodName) {
        return BY_NAME.get(methodName);
    }
}
