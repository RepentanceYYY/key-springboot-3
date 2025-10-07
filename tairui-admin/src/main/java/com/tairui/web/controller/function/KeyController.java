package com.tairui.web.controller.function;

import com.alibaba.fastjson2.JSONObject;
import com.tairui.common.annotation.Log;
import com.tairui.common.core.controller.BaseController;
import com.tairui.common.core.domain.AjaxResult;
import com.tairui.common.core.page.TableDataInfo;
import com.tairui.common.enums.BusinessType;
import com.tairui.common.utils.ServletUtils;
import com.tairui.common.utils.StringUtils;
import com.tairui.common.utils.poi.ExcelUtil;
import com.tairui.function.domain.Key;
import com.tairui.function.domain.vo.KeyAppVo;
import com.tairui.function.service.IKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.*;
import java.util.List;

/**
 * 钥匙Controller
 *
 * @author yhs
 * @date 2025-07-30
 */
@RestController
@RequestMapping("/function/key")
public class KeyController extends BaseController
{
    @Autowired
    private IKeyService keyService;

    /**
     * 查询钥匙列表
     */
    @PreAuthorize("@ss.hasPermi('function:key:list')")
    @GetMapping("/list")
    public TableDataInfo list(Key key)
    {
        startPage();
        List<Key> list = keyService.selectKeyList(key);
        return getDataTable(list);
    }

    /**
     * 一键绑定所有钥匙
     * @param srttings
     * @return
     */
    @PreAuthorize("@ss.hasPermi('function:key:bind')")
    @PostMapping("/batchBindKeys/{srttings}")
    public AjaxResult batchBindKeys(@PathVariable("srttings") String srttings)
    {

        return success(keyService.batchBindKeys(srttings));
    }

    /**
     * 一键解绑所有钥匙
     * @param srttings
     * @return
     */
    @PreAuthorize("@ss.hasPermi('function:key:unBind')")
    @PostMapping("/batchUnBindKeys/{srttings}")
    public AjaxResult batchUnBindKeys(@PathVariable("srttings") String srttings)
    {

        return success(keyService.batchUnBindKeys(srttings));
    }

    /**
     * 导出钥匙列表
     */
    @PreAuthorize("@ss.hasPermi('function:key:export')")
    @Log(title = "钥匙", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Key key)
    {
        List<Key> list = keyService.selectKeyList(key);
        ExcelUtil<Key> util = new ExcelUtil<Key>(Key.class);
        util.exportExcel(response, list, "钥匙数据");
    }

    /**
     * 钥匙初始化
     */
    @PreAuthorize("@ss.hasPermi('function:key:initKey')")
    @GetMapping(value = "/initKeyApi/{keyId}")
    public AjaxResult initKeyApi(@PathVariable("keyId") Long keyId)
    {

        return success(keyService.initKeyApi(keyId));
    }


    /**
     * 钥匙初始化
     */
    @PreAuthorize("@ss.hasPermi('function:key:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(keyService.selectKeyById(id));
    }


    /**
     * 新增钥匙
     */
    @PreAuthorize("@ss.hasPermi('function:key:add')")
    @Log(title = "钥匙", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Key key)
    {
        return toAjax(keyService.insertKey(key));
    }

    /**
     * 修改钥匙
     */
    @PreAuthorize("@ss.hasPermi('function:key:edit')")
    @Log(title = "钥匙", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Key key)
    {
        if (StringUtils.isNotEmpty(key.getName()) && StringUtils.isNotEmpty(key.getCode()) && StringUtils.isNotEmpty(key.getSrttings()) &&
                !keyService.checkKeyUnique(key))
        {
            return error("所属钥匙柜:'"+key.getSrttingsName()+"'钥匙名称:'"+key.getName()+"'和钥匙标号:'"+key.getCode()+"'已存在");
        }
        return toAjax(keyService.updateKey(key));
    }

    /**
     * 删除钥匙
     */
    @PreAuthorize("@ss.hasPermi('function:key:remove')")
    @Log(title = "钥匙", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(keyService.deleteKeyByIds(ids));
    }


    /**
     * 查询用户绑定的钥匙柜
     */
    @PostMapping("listKeyByUser")
    public AjaxResult listKeyByUser(@RequestBody Key key) {
        if (StringUtils.isEmpty(ServletUtils.getRequest().getHeader("X-User-Id"))) {
            return error("请求头中缺少用户id");
        }
        key.setUserId(ServletUtils.getRequest().getHeader("X-User-Id"));
        List<KeyAppVo> listKey = keyService.listKeyByUser(key);
        return success(listKey);
    }

    /**
     * 钥匙柜权限查询
     */
    @PostMapping("getKeyCabinetPermission")
    public AjaxResult getKeyCabinetPermission(@RequestParam(value = "userName", required = false) String userName) {
        if (StringUtils.isEmpty(userName)) {
            userName = ServletUtils.getRequest().getHeader("Authorization");
        }
        List<JSONObject> keyCabinetPermission = keyService.getKeyCabinetPermission(userName);
        return success(keyCabinetPermission);
    }


}
