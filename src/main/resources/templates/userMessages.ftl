<#import "parts/common.ftl" as c>

<@c.page>
    <#if isCurrentUser>
    <#include "parts/messageEdit.ftl" />
    <#include "parts/messageList.ftl" />
    </#if>
</@c.page>