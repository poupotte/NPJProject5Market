/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webShop.view;

import javax.inject.Named;
import javax.enterprise.context.ConversationScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.inject.Inject;
import webShop.controller.WebShopFacade;
import webShop.model.CustomerDTO;

/**
 *
 * @author zoe
 */
@Named(value = "webShopManager")
@ConversationScoped
public class WebShopManager implements Serializable {
 
    @EJB
    private WebShopFacade webShopFacade;
    private String currentPseudo;
    private String currentPassword;
    private Exception transactionFailure;
    @Inject
    private Conversation conversation;
    /**
     * Creates a new instance of webShopManager
     */
    public WebShopManager() {
    }
    
    private void handleException(Exception e) {
        stopConversation();
        e.printStackTrace(System.err);
        transactionFailure = e;
    }

    /**
     * Start the conversation with the bean
     */
    private void startConversation() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
    }

    /**
     * stop the conversation with the beans
     */
    private void stopConversation() {
        if (!conversation.isTransient()) {
            conversation.end();
        }
    }

    /**
     * Returns
     * <code>false</code> if the latest transaction failed, otherwise
     * <code>false</code>.
     */
    public boolean getSuccess() {
        return transactionFailure == null;
    }

    /**
     * Returns the latest thrown exception.
     */
    public Exception getException() {
        return transactionFailure;
    }
    
    public void loginCustomer(){
        startConversation();
        webShopFacade.loginCustomer(currentPseudo); 
    }
    
    public void createNewCustomer() {
        startConversation();
        try {
        CustomerDTO customer = webShopFacade.createCustomerDTO(currentPseudo, currentPassword);     
        } catch (Exception e) {
            handleException(e);
        }
    
    }

}
