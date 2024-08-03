package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;


    @Override
    public void save(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmeal.setStatus(StatusConstant.DISABLE);
        // 插入信息的套餐表
        setmealMapper.save(setmeal);

        Long setmealId = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes != null && setmealDishes.size()>0){
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealId);
            });
        }

        // 批量插入数据到套餐菜品关联表
        setmealDishMapper.insertBatch(setmealDishes);
    }

    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        long total = page.getTotal();
        List<SetmealVO> result = page.getResult();
        return new PageResult(total, result);
    }

    @Override
    public void delete(List<Long> ids) {
        // 检查是否有启售中的套餐
        List<Integer> statuses = setmealMapper.getByIds(ids);
        for(Integer status : statuses){
            if (status == StatusConstant.ENABLE){
                throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        // 批量删除套餐
        setmealMapper.delete(ids);
        // 批量删除套餐菜品关联表中的数据
        setmealDishMapper.delete(ids);
    }

    @Override
    public SetmealVO getById(Long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        List<SetmealDish> dishes = setmealDishMapper.getDishesBySetmealId(id);
        if (dishes != null && dishes.size()>0){
            setmealVO.setSetmealDishes(dishes);
        }
        return setmealVO;
    }

    @Override
    public void update(SetmealDTO setmealDTO) {
        // 更新套餐信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);

        // 删除原先绑定的套餐菜品信息
        Long setmealId = setmealDTO.getId();
        List<Long> setmealIds = new ArrayList<>();
        setmealIds.add(setmealId);
        setmealDishMapper.delete(setmealIds);
        // 添加套餐菜品关系表
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });
        setmealDishMapper.insertBatch(setmealDishes);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        if (status == StatusConstant.ENABLE){
            // 起售套餐时，如果套餐内包含停售的菜品，则不能起售
            List<SetmealDish> setmealDishes = setmealDishMapper.getDishesBySetmealId(id);
            ArrayList<Long> dishIds = new ArrayList<>();
            for (SetmealDish setmealDish : setmealDishes){
                dishIds.add(setmealDish.getDishId());
            }
            List<Dish> dishes = dishMapper.getByCategoryIds(dishIds);
            for (Dish dish : dishes){
                if (dish.getStatus() == StatusConstant.DISABLE){
                    throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            }

            // 起售
            Setmeal setmeal = new Setmeal();
            setmeal.setStatus(status);
            setmeal.setId(id);
            setmealMapper.update(setmeal);
        }else if (status == StatusConstant.DISABLE){
            // 停售
            Setmeal setmeal = new Setmeal();
            setmeal.setStatus(status);
            setmeal.setId(id);
            setmealMapper.update(setmeal);
        }

    }


    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
