import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IpFilter implements Filter {



    public static final String BLACK_LIST_IP = "/WEB-INF/resource/blackList.txt";
    private FilterConfig filterConfig;
    private Map<String, String> mapIP = new ConcurrentHashMap<>();
    long lastModified;


    public IpFilter() {}

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        String userIp = servletRequest.getRemoteAddr();
        ServletContext context = servletRequest.getServletContext();
        URL fileName = context.getResource(BLACK_LIST_IP);


        Path path = null;

        try {
            path = Paths.get(fileName.toURI());
            File file = path.toFile();
            if (lastModified != file.lastModified()){
                mapIP.clear();
                lastModified = file.lastModified();
                readBlackList(path);
            }
        }catch (URISyntaxException e){}


        HttpServletResponse httpResponse = null;
        if (servletResponse instanceof HttpServletResponse){
            httpResponse = (HttpServletResponse) servletResponse;
        }


        if (mapIP.containsKey(userIp)){
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access disallowed!");
        }else {
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }

    private void readBlackList(Path path){
        try (Stream <String> stream = Files.lines(path)) {
            mapIP = stream
                    .filter(line -> !line.startsWith("#"))
                    .map(String::toUpperCase)
                    .collect(Collectors.toMap(line -> line, line -> line));
        }catch (IOException e){}
    }


    @Override
    public void destroy() {

    }

}

