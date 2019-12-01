package model.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class ServiceMap {
    private HashMap<String, ActivatedService> serviceHashMap = new HashMap<>();

    public ServiceMap(ActivatedService... activatedServices) {
        for(ActivatedService activatedService: activatedServices)
            serviceHashMap.put(activatedService.getType(), activatedService);
    }

    public ServiceMap() {

    }

    public void put(ActivatedService activatedService) {
        String serviceType = activatedService.getType();
        serviceHashMap.put(serviceType, activatedService);
    }

    public ActivatedService get(String serviceType) {
        ActivatedService activatedService = serviceHashMap.get(serviceType);
        if (activatedService == null)
            throw new IllegalArgumentException("Service type not found!");
        return activatedService;
    }

    public void remove(String serviceType) {
        serviceHashMap.remove(serviceType);
    }

    public String[] getTypes() {
        Set<String> set = serviceHashMap.keySet();
        String[] array = new String[set.size()];
        set.toArray(array);
        return array;
    }

    public ActivatedService[] getServices() {
        Collection<ActivatedService> arrayList = serviceHashMap.values();
        ActivatedService[] array = new ActivatedService[arrayList.size()];
        arrayList.toArray(array);
        return array;
    }

}
