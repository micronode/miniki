<% def node = request.getAttribute('mn:node') %>
<h2 style="color:lightgray">
<% 
    def breadcrumb
    breadcrumb = { n ->
        if (!n.parent.isSame(n.session.rootNode)) {
            breadcrumb(n.parent)
        }
        %>&nbsp;&raquo&nbsp;<%
        html.a(href: "/view${n.path - /mn:content\/?/}", style: 'color:gray', n['mn:title'].string)
    }
    breadcrumb(node)
%>
<a href="/history<%= node.path%>" style="color:gray;vertical-align:super;font-size:0.5em">[History]</a>
<a href="/edit<%= node.path%>" style="color:gray;vertical-align:super;font-size:0.5em">[Edit]</a>
</h2>
