/*
 * Copyright 1999-2024 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.alibaba.nacos.console.proxy;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.common.model.RestResult;
import com.alibaba.nacos.config.server.controller.parameters.SameNamespaceCloneConfigBean;
import com.alibaba.nacos.config.server.model.ConfigAllInfo;
import com.alibaba.nacos.config.server.model.ConfigInfo;
import com.alibaba.nacos.config.server.model.ConfigRequestInfo;
import com.alibaba.nacos.config.server.model.GroupkeyListenserStatus;
import com.alibaba.nacos.config.server.model.SameConfigPolicy;
import com.alibaba.nacos.config.server.model.form.ConfigForm;
import com.alibaba.nacos.console.config.ConsoleConfig;
import com.alibaba.nacos.console.handler.ConfigHandler;
import com.alibaba.nacos.console.handler.inner.ConfigInnerHandler;
import com.alibaba.nacos.persistence.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Proxy class for handling configuration operations.
 *
 * @author zhangyukun
 */
@Service
public class ConfigProxy {
    
    private final Map<String, ConfigHandler> configHandlerMap = new HashMap<>();
    
    private final ConsoleConfig consoleConfig;
    
    @Autowired
    public ConfigProxy(ConfigInnerHandler configInnerHandler, ConsoleConfig consoleConfig) {
        this.configHandlerMap.put("merged", configInnerHandler);
        this.consoleConfig = consoleConfig;
    }
    
    /**
     * Retrieves configuration based on the provided parameters.
     *
     */
    public void getConfig(HttpServletRequest request, HttpServletResponse response, String dataId, String group,
            String namespaceId, String tag, String isNotify, String clientIp, boolean isV2)
            throws IOException, ServletException, NacosException {
        ConfigHandler configHandler = configHandlerMap.get(consoleConfig.getType());
        if (configHandler == null) {
            throw new NacosException(NacosException.INVALID_PARAM, "Invalid deployment type");
        }
        configHandler.getConfig(request, response, dataId, group, namespaceId, tag, isNotify, clientIp, isV2);
    }
    
    /**
     * Retrieves detailed configuration information.
     *
     * @return ConfigAllInfo
     */
    public ConfigAllInfo detailConfigInfo(String dataId, String group, String namespaceId) throws NacosException {
        ConfigHandler configHandler = configHandlerMap.get(consoleConfig.getType());
        if (configHandler == null) {
            throw new NacosException(NacosException.INVALID_PARAM, "Invalid deployment type");
        }
        return configHandler.detailConfigInfo(dataId, group, namespaceId);
    }
    
    /**
     * Publishes the configuration based on the provided form and request information.
     *
     * @return Boolean
     */
    public Boolean publishConfig(ConfigForm configForm, ConfigRequestInfo configRequestInfo) throws NacosException {
        ConfigHandler configHandler = configHandlerMap.get(consoleConfig.getType());
        if (configHandler == null) {
            throw new NacosException(NacosException.INVALID_PARAM, "Invalid deployment type");
        }
        return configHandler.publishConfig(configForm, configRequestInfo);
    }
    
    /**
     * Deletes the configuration based on the provided parameters.
     *
     * @return Boolean
     */
    public Boolean deleteConfig(String dataId, String group, String namespaceId, String tag, String clientIp,
            String srcUser) throws NacosException {
        ConfigHandler configHandler = configHandlerMap.get(consoleConfig.getType());
        if (configHandler == null) {
            throw new NacosException(NacosException.INVALID_PARAM, "Invalid deployment type");
        }
        return configHandler.deleteConfig(dataId, group, namespaceId, tag, clientIp, srcUser);
    }
    
    /**
     * Deletes multiple configurations based on the provided IDs.
     *
     * @return Boolean
     */
    public Boolean deleteConfigs(List<Long> ids, String clientIp, String srcUser) throws NacosException {
        ConfigHandler configHandler = configHandlerMap.get(consoleConfig.getType());
        if (configHandler == null) {
            throw new NacosException(NacosException.INVALID_PARAM, "Invalid deployment type");
        }
        return configHandler.deleteConfigs(ids, clientIp, srcUser);
    }
    
    /**
     * Searches for configurations based on the provided details.
     *
     * @return ConfigInfo
     */
    public Page<ConfigInfo> searchConfigByDetails(String search, int pageNo, int pageSize, String dataId, String group,
            String namespaceId, Map<String, Object> configAdvanceInfo) throws NacosException {
        ConfigHandler configHandler = configHandlerMap.get(consoleConfig.getType());
        if (configHandler == null) {
            throw new NacosException(NacosException.INVALID_PARAM, "Invalid deployment type");
        }
        return configHandler.searchConfigByDetails(search, pageNo, pageSize, dataId, group, namespaceId, configAdvanceInfo);
    }
    
    /**
     * Retrieves the status of the configuration listeners.
     *
     * @return GroupkeyListenserStatus
     */
    public GroupkeyListenserStatus getListeners(String dataId, String group, String namespaceId, int sampleTime)
            throws Exception {
        ConfigHandler configHandler = configHandlerMap.get(consoleConfig.getType());
        if (configHandler == null) {
            throw new NacosException(NacosException.INVALID_PARAM, "Invalid deployment type");
        }
        return configHandler.getListeners(dataId, group, namespaceId, sampleTime);
    }
    
    /**
     * Imports and publishes a configuration from a file.
     *
     * @return RestResult
     */
    public RestResult<Map<String, Object>> importAndPublishConfig(String srcUser, String namespaceId, SameConfigPolicy policy,
            MultipartFile file, String srcIp, String requestIpApp) throws NacosException {
        ConfigHandler configHandler = configHandlerMap.get(consoleConfig.getType());
        if (configHandler == null) {
            throw new NacosException(NacosException.INVALID_PARAM, "Invalid deployment type");
        }
        return configHandler.importAndPublishConfig(srcUser, namespaceId, policy, file, srcIp, requestIpApp);
    }
    
    /**
     * Clones the configuration based on the provided parameters.
     *
     * @return RestResult
     */
    public RestResult<Map<String, Object>> cloneConfig(String srcUser, String namespaceId,
            List<SameNamespaceCloneConfigBean> configBeansList, SameConfigPolicy policy, String srcIp, String requestIpApp) throws NacosException {
        ConfigHandler configHandler = configHandlerMap.get(consoleConfig.getType());
        if (configHandler == null) {
            throw new NacosException(NacosException.INVALID_PARAM, "Invalid deployment type");
        }
        return configHandler.cloneConfig(srcUser, namespaceId, configBeansList, policy, srcIp, requestIpApp);
    }
}
