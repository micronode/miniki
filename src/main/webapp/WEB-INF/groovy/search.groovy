import org.mnode.juicer.query.QueryBuilder

if (request.getParameter('q')) {
    def jcr = context.getAttribute('jcr')
    def searchQuery = new QueryBuilder(jcr.workspace.queryManager, jcr.valueFactory).with {
        query(
            source: selector(nodeType: 'nt:unstructured', name: 'items'),
            constraint: and(
                constraint1: descendantNode(selectorName: 'items', path: '/mn:content'),
                constraint2: fullTextSearch(selectorName: 'items', propertyName: 'mn:text', searchTerms: request.getParameter('q'))
            )
        )
    }
    request.setAttribute('query', searchQuery)
    
    def attachSearchQuery = new QueryBuilder(jcr.workspace.queryManager, jcr.valueFactory).with {
        query(
                source: selector(nodeType: 'nt:unstructured', name: 'items'),
                constraint: and(
                        constraint1: descendantNode(selectorName: 'items', path: '/mn:attachments'),
                        constraint2: fullTextSearch(selectorName: 'items', propertyName: 'jcr:content', searchTerms: request.getParameter('q'))
                        )
                )
    }
    request.setAttribute('attachQuery', attachSearchQuery)
}

forward("search.html")