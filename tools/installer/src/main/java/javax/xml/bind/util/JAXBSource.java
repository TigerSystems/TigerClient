/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2003-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.xml.bind.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.sax.SAXSource;

import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * JAXP {@link javax.xml.transform.Source} implementation
 * that marshals a JAXB-generated object.
 * 
 * <p>
 * This utility class is useful to combine JAXB with
 * other Java/XML technologies.
 * 
 * <p>
 * The following example shows how to use JAXB to marshal a document
 * for transformation by XSLT.
 * 
 * <blockquote>
 *    <pre>
 *       MyObject o = // get JAXB content tree
 *       
 *       // jaxbContext is a JAXBContext object from which 'o' is created.
 *       JAXBSource source = new JAXBSource( jaxbContext, o );
 *       
 *       // set up XSLT transformation
 *       TransformerFactory tf = TransformerFactory.newInstance();
 *       Transformer t = tf.newTransformer(new StreamSource("test.xsl"));
 *       
 *       // run transformation
 *       t.transform(source,new StreamResult(System.out));
 *    </pre>
 * </blockquote>
 * 
 * <p>
 * The fact that JAXBSource derives from SAXSource is an implementation
 * detail. Thus in general applications are strongly discouraged from
 * accessing methods defined on SAXSource. In particular,
 * the setXMLReader and setInputSource methods shall never be called.
 * The XMLReader object obtained by the getXMLReader method shall
 * be used only for parsing the InputSource object returned by
 * the getInputSource method.
 * 
 * <p>
 * Similarly the InputSource object obtained by the getInputSource
 * method shall be used only for being parsed by the XMLReader object
 * returned by the getXMLReader.
 *
 * @author
 * 	Kohsuke Kawaguchi (kohsuke.kawaguchi@sun.com)
 * @since 1.6
 */
public class JAXBSource extends SAXSource {

    /**
     * Creates a new {@link javax.xml.transform.Source} for the given content object.
     * 
     * @param   context
     *      JAXBContext that was used to create
     *      <code>contentObject</code>. This context is used
     *      to create a new instance of marshaller and must not be null.
     * @param   contentObject
     *      An instance of a JAXB-generated class, which will be
     *      used as a {@link javax.xml.transform.Source} (by marshalling it into XML).  It must
     *      not be null.
     * @throws JAXBException if an error is encountered while creating the
     * JAXBSource or if either of the parameters are null.
     */
    public JAXBSource( JAXBContext context, Object contentObject ) 
        throws JAXBException {
            
        this( 
            ( context == null ) ? 
                assertionFailed( Messages.format( Messages.SOURCE_NULL_CONTEXT ) ) : 
                context.createMarshaller(),
                
            ( contentObject == null ) ? 
                assertionFailed( Messages.format( Messages.SOURCE_NULL_CONTENT ) ) : 
                contentObject);
    }
    
    /**
     * Creates a new {@link javax.xml.transform.Source} for the given content object.
     * 
     * @param   marshaller
     *      A marshaller instance that will be used to marshal
     *      <code>contentObject</code> into XML. This must be
     *      created from a JAXBContext that was used to build
     *      <code>contentObject</code> and must not be null.
     * @param   contentObject
     *      An instance of a JAXB-generated class, which will be
     *      used as a {@link javax.xml.transform.Source} (by marshalling it into XML).  It must
     *      not be null.
     * @throws JAXBException if an error is encountered while creating the
     * JAXBSource or if either of the parameters are null.
     */
    public JAXBSource( Marshaller marshaller, Object contentObject ) 
        throws JAXBException {
            
        if( marshaller == null )
            throw new JAXBException( 
                Messages.format( Messages.SOURCE_NULL_MARSHALLER ) );
                
        if( contentObject == null )
            throw new JAXBException( 
                Messages.format( Messages.SOURCE_NULL_CONTENT ) );
            
        this.marshaller = marshaller;
        this.contentObject = contentObject;
        
        super.setXMLReader(pseudoParser);
        // pass a dummy InputSource. We don't care
        super.setInputSource(new InputSource());
    }
    
    private final Marshaller marshaller;
    private final Object contentObject;
    
    // this object will pretend as an XMLReader.
    // no matter what parameter is specified to the parse method,
    // it just parse the contentObject.
    private final XMLReader pseudoParser = new XMLReader() {
        public boolean getFeature(String name) throws SAXNotRecognizedException {
            if(name.equals("http://xml.org/sax/features/namespaces"))
                return true;
            if(name.equals("http://xml.org/sax/features/namespace-prefixes"))
                return false;
            throw new SAXNotRecognizedException(name);
        }

        public void setFeature(String name, boolean value) throws SAXNotRecognizedException {
            if(name.equals("http://xml.org/sax/features/namespaces") && value)
                return;
            if(name.equals("http://xml.org/sax/features/namespace-prefixes") && !value)
                return;
            throw new SAXNotRecognizedException(name);
        }

        public Object getProperty(String name) throws SAXNotRecognizedException {
            if( "http://xml.org/sax/properties/lexical-handler".equals(name) ) {
                return lexicalHandler;
            }
            throw new SAXNotRecognizedException(name);
        }

        public void setProperty(String name, Object value) throws SAXNotRecognizedException {
            if( "http://xml.org/sax/properties/lexical-handler".equals(name) ) {
                this.lexicalHandler = (LexicalHandler)value;
                return;
            }
            throw new SAXNotRecognizedException(name);
        }

        private LexicalHandler lexicalHandler;

        // we will store this value but never use it by ourselves.
        private EntityResolver entityResolver;
        public void setEntityResolver(EntityResolver resolver) {
            this.entityResolver = resolver;
        }
        public EntityResolver getEntityResolver() {
            return entityResolver;
        }

        private DTDHandler dtdHandler;
        public void setDTDHandler(DTDHandler handler) {
            this.dtdHandler = handler;
        }
        public DTDHandler getDTDHandler() {
            return dtdHandler;
        }

        // SAX allows ContentHandler to be changed during the parsing,
        // but JAXB doesn't. So this repeater will sit between those
        // two components.
        private XMLFilter repeater = new XMLFilterImpl();

        public void setContentHandler(ContentHandler handler) {
            repeater.setContentHandler(handler);
        }
        public ContentHandler getContentHandler() {
            return repeater.getContentHandler();
        }

        private ErrorHandler errorHandler;
        public void setErrorHandler(ErrorHandler handler) {
            this.errorHandler = handler;
        }
        public ErrorHandler getErrorHandler() {
            return errorHandler;
        }

        public void parse(InputSource input) throws SAXException {
            parse();
        }

        public void parse(String systemId) throws SAXException {
            parse();
        }

        public void parse() throws SAXException {
            // parses a content object by using the given marshaller
            // SAX events will be sent to the repeater, and the repeater
            // will further forward it to an appropriate component.
            try {
                marshaller.marshal( contentObject, (XMLFilterImpl)repeater );
            } catch( JAXBException e ) {
                // wrap it to a SAXException
                SAXParseException se =
                    new SAXParseException( e.getMessage(),
                        null, null, -1, -1, e );

                // if the consumer sets an error handler, it is our responsibility
                // to notify it.
                if(errorHandler!=null)
                    errorHandler.fatalError(se);

                // this is a fatal error. Even if the error handler
                // returns, we will abort anyway.
                throw se;
            }
        }
    };

    /**
     * Hook to throw exception from the middle of a contructor chained call
     * to this
     */
    private static Marshaller assertionFailed( String message ) 
        throws JAXBException {
            
        throw new JAXBException( message );
    }
}
