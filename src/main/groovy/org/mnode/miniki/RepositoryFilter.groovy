package org.mnode.miniki

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.jackrabbit.util.Text;

class RepositoryFilter implements Filter {

    void init(FilterConfig config) throws ServletException {
        // TODO Auto-generated method stub

    }

    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
    ServletException {

        request.with {
            def node
            if (getParameter('p') && servletContext.getAttribute('jcr').itemExists(getParameter('p'))) {
                node = servletContext.getAttribute('jcr').getNode(getParameter('p'))
            }
            else {
                node = servletContext.getAttribute('jcr').rootNode << 'mn:content'
                node.session.save {
//                    node.addMixin("mix:versionable")
                    if (!node['mn:title']) {
                        node['mn:title'] = 'My Miniki'
                    }
                }
                /*
                if (node.session.workspace.versionManager.isCheckedOut(node.path)) {
                    node.session.save {
                        node.addMixin("mix:versionable")
                        if (!node['mn:title']) {
                            node['mn:title'] = 'My Miniki'
                        }
                    }
                    node.checkin()
                }
                */
            }
            
            if (request.getParameter('c')) {
                def title = request.getParameter('c')
//                if (node.isNodeType('mix:versionable')) {
//                    node.checkout()
//                }
                node = node << Text.escapeIllegalJcrChars(title)
                node.addMixin('mix:versionable')
                node.session.save {
                    node['mn:title'] = title
                }
//                node.checkin()
//                if (node.parent.isNodeType('mix:versionable')) {
//                    node.parent.checkin()
//                }
           }
        
            setAttribute 'mn:node', node
        }
        
        chain.doFilter(request, response);
    }

    void destroy() {
        // TODO Auto-generated method stub

    }
}
