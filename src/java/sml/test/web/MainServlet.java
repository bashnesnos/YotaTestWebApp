/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sml.test.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import sml.test.ejb.PropertyFacadeLocal;
import sml.test.model.Property;
import sml.test.model.Root;

/**
 *
 * @author asemelit
 */
@WebServlet(name = "MainServlet", urlPatterns = {MainServlet.UPLOAD_URL, MainServlet.DOWNLOAD_URL})
@MultipartConfig
public class MainServlet extends HttpServlet {
    public static final String UPLOAD_URL = "/upload";
    public static final String DOWNLOAD_URL = "/download";
    
    private final static JAXBContext context;
    
    static {
        try {
            context = JAXBContext.newInstance(Root.class);
        } catch (JAXBException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @EJB(beanName = "PropertyFacade")
    private PropertyFacadeLocal propertyFacade;
    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (DOWNLOAD_URL.equals(request.getServletPath())) {
            Root root = new Root();
            List<Property> topProps = propertyFacade.findAllTop();
            System.out.println(topProps);
            root.getProperties().addAll(topProps);
            
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
        else {
            throw new RuntimeException("Wrong GET URL: " + request.getServletPath());
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
            if (UPLOAD_URL.equals(request.getServletPath())) {
                Part filePart = request.getPart("file");
                if (filePart.getSubmittedFileName().isEmpty()) {
                    throw new ExpectedException("не был выбран файл");
                }
                if (filePart.getSize() == 0) {
                    throw new ExpectedException("файл пуст");
                }
                InputStream fileContent = new BufferedInputStream(filePart.getInputStream(), 4096);
                try {
                    Unmarshaller rootUnmarshaller = context.createUnmarshaller();
                    Root unmarshalled = (Root) rootUnmarshaller.unmarshal(fileContent);
                    for (Property unmarshalledProp : unmarshalled.getProperties()) {
                        List<Property> candidates = propertyFacade.findTopByName(unmarshalledProp.getName()); //actually, it will merge all your properties with the same name and value into one hieararchy
                        
                        if (candidates.isEmpty() || !candidates.contains(unmarshalledProp)) {
                            Property merged = null;
                            for (Property mergeCandidate : candidates) {
                                if (mergeCandidate.merge(unmarshalledProp)) {
                                    merged = mergeCandidate;
                                    break;
                                }
                            }
                            if (merged != null) {
                                propertyFacade.edit(merged);
                            }
                            else {
                                propertyFacade.create(unmarshalledProp);
                            }
                        }
                        else {
                            System.out.println("Attempt to duplicate: " + unmarshalledProp);
                        }
                    }
                } 
                catch (JAXBException ex) {
                    throw new RuntimeException("Ошибка при обработке xml", ex);
                }
                response.setContentType("text/html;charset=UTF-8");
                try (PrintWriter out = response.getWriter()) {
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Успешно</title>");            
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<h1>Загрузка успешна</h1>");
                    out.println("<a href='" + request.getContextPath() + "'>Вернуться</a>");
                    out.println("</body>");
                    out.println("</html>");
                    out.close();
                }
            }
            else {
                throw new RuntimeException("Wrong POST URL: " + request.getServletPath());
            }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Upload/download handler";
    }

}
