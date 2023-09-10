package com.gexingw.shop.api.user.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/9/8 16:48
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UserVO implements Serializable {

    private static final long serialVersionUID = 7684072485376517231L;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String name;

    public UserVO(Long id) {
        this.id = id;
    }

}
