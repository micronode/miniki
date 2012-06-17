<% def node = request.getAttribute('mn:node') %>
<h2 style="color:lightgray">
<% 
    def breadcrumb
    breadcrumb = { n ->
        if (!n.parent.isSame(n.session.rootNode)) {
            breadcrumb(n.parent)
        }
        
%>&nbsp;&raquo&nbsp;<%

        out << miniki.links.view(n)
    }
    breadcrumb(node)
%>
<%= miniki.links.history(node) %>
<%= miniki.links.edit(node) %>
</h2>
