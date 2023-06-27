package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {
    private Map<String, Order> orderMap;
    private Map<String,String>orderPartnerDatabase;

    private Map<String,DeliveryPartner> partnerMap;
    private Map<String, Set<String>> partnerOrderDatabase;

    public OrderRepository(){
        orderMap = new HashMap<>();
        orderPartnerDatabase = new HashMap<>();
        partnerMap = new HashMap<>();
        partnerOrderDatabase = new HashMap<>();
    }



    public Order getOrder(String orderId)  {

        return orderMap.get(orderId);
    }

    public void addPartner(String deliveryPartnerId){
        DeliveryPartner deliveryPartner = new DeliveryPartner(deliveryPartnerId);
        partnerMap.put(deliveryPartner.getId(),deliveryPartner);

    }

    public Set<String> getOrderIdsByPartnerId(String partnerId) {


        return partnerOrderDatabase.getOrDefault(partnerId,null);
    }

    public void addOrderPartnerPair(String orderId, String partnerId)  {


        DeliveryPartner partner = partnerMap.get(partnerId);
        if(partner==null) {

            return;
        }


        Set<String> set =  partnerOrderDatabase.getOrDefault(partner.getId(),new HashSet<>()); // add new order to the orderlist
        set.add(orderId);

        partner.setNumberOfOrders(set.size()); // update

        partnerOrderDatabase.put(partner.getId(),set);
        orderPartnerDatabase.put(orderId,partner.getId());

    }

    public DeliveryPartner getPartner(String partnerId) {

        return partnerMap.get(partnerId);

    }

    public void addOrder(Order order){
        orderMap.put(order.getId(),order);
        orderPartnerDatabase.put(order.getId(),"unassigned");
    }

    public List<String> getAllOrders() {
        List<String> list = new ArrayList<>();

        for(String oid: orderMap.keySet())
            list.add(oid);
        return list;
    }


    public Integer getUnassignedOrderCount() {
        Integer count = 0;
//        if(orderPartnerDatabase.size()==0)
//            return count;

        for(String s: orderPartnerDatabase.values())
            if(s.equals("unassigned"))
                count++;
        return count;

    }



    public void deletePartnerById(String partnerId) {

        for(String orderId: orderPartnerDatabase.keySet()){
            if(orderPartnerDatabase.get(orderId).equals(partnerId)){
                orderPartnerDatabase.put(orderId,"unassigned");
            }
        }

        partnerMap.remove(partnerId);
        partnerOrderDatabase.remove(partnerId);
    }

    public void deleteOrderById(String orderId) {
        if(!orderMap.containsKey(orderId))
            return;
        orderMap.remove(orderId);
        String partnerId = orderPartnerDatabase.get(orderId);
        System.out.println(orderPartnerDatabase);

        if(partnerId.equals("unassigned"))
            return;

        Set<String> set = partnerOrderDatabase.get(partnerId);
        set.remove(orderId);
//        for(String oid: list ){
//            if(oid.equals(orderId)){
//                list.remove(orderId);
//            }
//        }
        DeliveryPartner partner = partnerMap.get(partnerId);
        partner.setNumberOfOrders(set.size());
        partnerOrderDatabase.put(partnerId,set);

        orderPartnerDatabase.remove(orderId);


    }
}