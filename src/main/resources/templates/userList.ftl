<#import "parts/common.ftl" as c>

<@c.page>
List of users

<table>
    <thead>
    <tr>
        <th>Name</th>
        <th>Role</th>
        <th></th>
    </tr>
    </thead>
    <tbody>
    <#if users??><#list users as user>
        <tr>
            <td>${(user.username)!}</td>
            <td><#list user.roles as role>${role}<#sep>, </#list></td>
            <td><a href="/user/${user.id}">edit</a></td>
        </tr>
    </#list>
    </#if>
    </tbody>
</table>
</@c.page>