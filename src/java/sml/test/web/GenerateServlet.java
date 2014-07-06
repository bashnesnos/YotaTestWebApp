/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sml.test.web;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import sml.test.model.PropertyGenerator;
import sml.test.model.Root;

/**
 *
 * @author asemelit
 */
@WebServlet(name = "GenerateServlet", urlPatterns = {GenerateServlet.GENERATE_URL})
@MultipartConfig
public class GenerateServlet extends HttpServlet {
    public static final String GENERATE_URL = "/generate";
    
    public static final Pattern WIDTH_PATRN = Pattern.compile("[1-9]|[1-4][0-9]");
    public static final Pattern DEPTH_PATRN = Pattern.compile("[1-9]|1[0-9]");
    
    private final static JAXBContext context;
    
    static {
        try {
            context = JAXBContext.newInstance(Root.class);
        } catch (JAXBException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int width = 49;
        int depth = 19;
        
        String widthParam = request.getParameter("w");
        String depthParam = request.getParameter("d");
        
        
        if (widthParam != null && WIDTH_PATRN.matcher(widthParam).matches()) {
            width = Integer.valueOf(widthParam);
        }

        if (depthParam != null && DEPTH_PATRN.matcher(depthParam).matches()) {
            depth = Integer.valueOf(depthParam);
        }

        PropertyGenerator propertyGenerator = new PropertyGenerator(width, depth);
        
        Root root = propertyGenerator.getRoot();
        
        if (root.getProperties().isEmpty()) {
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Скачать</title>");            
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Скачивать нечего</h1>");
                out.println("<a href='" + request.getContextPath() + "'>Вернуться</a>");
                out.println("</body>");
                out.println("</html>");
                out.close();
            }
        }
        else {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=properties.xml");
            try {
                Marshaller rootMarshaller = context.createMarshaller();
                rootMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                try (OutputStream output = new BufferedOutputStream(response.getOutputStream(), 4096)) {
                    rootMarshaller.marshal(root, output);
                }
            } catch (JAXBException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(request.getContextPath());
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Generation handler";
    }

}
