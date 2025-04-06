package com.smarthealth.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.smarthealth.domain.Entity.UserInfo;
import com.smarthealth.domain.VO.CategoryByAgeVO;
import com.smarthealth.domain.VO.GenderVO;
import com.smarthealth.mapper.UserInfoMapper;
import com.smarthealth.service.UserInfoService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    //统计男性、女性的人数
    public GenderVO getGenderNum() {
        LambdaQueryWrapper<UserInfo> maleWrapper = new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getGender, 1).eq(UserInfo::getIsDeleted, 0);
        LambdaQueryWrapper<UserInfo> femaleWrapper = new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getGender, 0).eq(UserInfo::getIsDeleted, 0);
        return GenderVO.builder()
                .num_male(count(maleWrapper))
                .num_female(count(femaleWrapper))
                .build();
    }


    //统计不同年龄段的人数
    public CategoryByAgeVO categoryByAge() {
        //查询所有的用户信息
        List<UserInfo> list = list(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getIsDeleted, 0));

        HashMap<String, Long> ageGroupCount = new HashMap<>();
        for(int i = 0 , j = 10; i < 7; i++ , j += 10){
            ageGroupCount.put(String.valueOf(j-10)+"-"+String.valueOf(j), 0L);
            if(i == 6)
                ageGroupCount.put(">60", 0L);
        }
        for (UserInfo userInfo : list) {
            int age = userInfo.getAge();
            if(age<=10)
                ageGroupCount.put("0-10", ageGroupCount.get("0-10")+1);
            else if(age<=20)
                ageGroupCount.put("10-20", ageGroupCount.get("10-20")+1);
            else if(age<=30)
                ageGroupCount.put("20-30", ageGroupCount.get("20-30")+1);
            else if(age<=40)
                ageGroupCount.put("30-40", ageGroupCount.get("30-40")+1);
            else if(age<=50)
                ageGroupCount.put("40-50", ageGroupCount.get("40-50")+1);
            else if(age<=60)
                ageGroupCount.put("50-60", ageGroupCount.get("50-60")+1);
            else
                ageGroupCount.put(">60", ageGroupCount.get(">60")+1);
        }

        String[] strings = joinMapKeysAndValues(ageGroupCount);
        return CategoryByAgeVO.builder()
                .categoryList(strings[0])
                .numberList(strings[1])
                .build();
    }




    /**
     * 将 Map 的 key 和 value 以逗号分隔合并成两个字符串
     * @param map Map 集合
     * @return 返回一个数组，第一个元素是 keys 的字符串，第二个元素是 values 的字符串
     */
    public static String[] joinMapKeysAndValues(Map<?, ?> map) {
        StringJoiner keyJoiner = new StringJoiner(",");
        StringJoiner valueJoiner = new StringJoiner(",");
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            keyJoiner.add(entry.getKey().toString());
            valueJoiner.add(entry.getValue().toString());
        }
        return new String[]{keyJoiner.toString(), valueJoiner.toString()};
    }


}
