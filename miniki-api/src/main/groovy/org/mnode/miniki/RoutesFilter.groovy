package org.mnode.miniki

import javax.servlet.*

class RoutesFilter implements Filter {

    void init(FilterConfig config) throws ServletException {
        // TODO Auto-generated method stub
        
    }
    
    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        chain.doFilter(request, response);
    }
            
    void destroy() {
        // TODO Auto-generated method stub
        
    }
}
