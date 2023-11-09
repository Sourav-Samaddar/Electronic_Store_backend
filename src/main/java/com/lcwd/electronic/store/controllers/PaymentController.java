package com.lcwd.electronic.store.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.PaymentLinkResponse;
import com.lcwd.electronic.store.entities.Order;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.OrderRepository;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@RestController
@RequestMapping("/payments")
public class PaymentController {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Value("${domain.name}")
	private String domainName;

	@PostMapping("/razor/{orderId}")
	public ResponseEntity<PaymentLinkResponse>createPaymentLink(@PathVariable String orderId) 
					throws RazorpayException{
		Order order = orderRepository.findById(orderId).
				orElseThrow(()->new ResourceNotFoundException("No orders available with given id !!"));
		User user = order.getUser();
		
		// Instantiate a Razorpay client with your key ID and secret
	    RazorpayClient razorpay = new RazorpayClient("rzp_test_9gaIJ4cMe7uPd6", "FaUSvS97IquyYDzqRLzzF8MJ");
	    
	    // Create a JSON object with the payment link request parameters
	    JSONObject paymentLinkRequest = new JSONObject();
	    paymentLinkRequest.put("amount",order.getBillingAmount()*100);
	    paymentLinkRequest.put("currency","INR");
	    
	    // Create a JSON object with the customer details
	    JSONObject customer = new JSONObject();
	    customer.put("name",user.getName());
	    customer.put("contact",order.getBillingPhone());
	    customer.put("email",user.getEmail());
	    paymentLinkRequest.put("customer",customer);
	    
	    // Create a JSON object with the notification settings
	    JSONObject notify = new JSONObject();
	    notify.put("sms",true);
	    notify.put("email",true);
	    paymentLinkRequest.put("notify",notify);
	    
	    // Set the reminder settings
	    paymentLinkRequest.put("reminder_enable",true);
	    
	    // Set the callback URL and method
	    paymentLinkRequest.put("callback_url",domainName+"/users/orders/"+orderId+"/paymentsuccess");
	    paymentLinkRequest.put("callback_method","get");

	    // Create the payment link using the paymentLink.create() method
	    PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);
	      
	    String paymentLinkId = payment.get("id");
	    String paymentLinkUrl = payment.get("short_url");
	      
	    PaymentLinkResponse res=new PaymentLinkResponse(paymentLinkUrl,paymentLinkId);
	      
	    PaymentLink fetchedPayment = razorpay.paymentLink.fetch(paymentLinkId);
	     
	    
	    // Print the payment link ID and URL
//	    System.out.println("Payment link ID: " + paymentLinkId);
//	    System.out.println("Payment link URL: " + paymentLinkUrl);
//	    System.out.println("Order Id : "+fetchedPayment.get("order_id")+fetchedPayment);
	    	      
	    return new ResponseEntity<PaymentLinkResponse>(res,HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/razor/{paymentId}/order/{orderId}")
    public ResponseEntity<ApiResponseMessage> paymentStatusandUpdateOrder(@PathVariable String paymentId, 
    		@PathVariable String orderId) throws RazorpayException {
		RazorpayClient razorpay = new RazorpayClient("rzp_test_9gaIJ4cMe7uPd6", "FaUSvS97IquyYDzqRLzzF8MJ");

		  try {
				Payment payment = razorpay.payments.fetch(paymentId);
				//System.out.println("payment details --- "+payment+payment.get("status"));
				
				if(payment.get("status").equals("captured")) {
					//System.out.println("payment details --- "+payment+payment.get("status"));
					Order order = orderRepository.findById(orderId).
							orElseThrow(()->new ResourceNotFoundException("No orders available with given id !!"));
					order.setPaymentId(paymentId);
					order.setPaymentStatus("PAID");
					orderRepository.save(order);
				}
				return new ResponseEntity<ApiResponseMessage>(ApiResponseMessage
						.builder()
						.message("Payment Successful !!")
						.success(true)
						.status(HttpStatus.OK)
						.build(),HttpStatus.OK);
	      
			} catch (Exception e) {
				throw new RazorpayException(e.getMessage());
			}
	}

	
}
