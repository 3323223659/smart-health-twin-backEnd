package com.smarthealth.domain.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryByAgeVO {

    //年龄的分段，以逗号分隔   "10,20,30,40"
    private String categoryList;

    //该年龄段的人数，以逗号分隔   "234,342,234,2342"
    private String numberList;
}
