<#macro a href='' class='' style=''>
  <a<#if href?has_content> href="${context.path}${href}"</#if><@css_class_style class style/>><#nested/></a><#t>
</#macro>

<#macro css_class_style class='' style=''>
  <#if class?has_content> class="${class}"</#if><#if style?has_content> style="${style}"</#if><#t>
</#macro>
