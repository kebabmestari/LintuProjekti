<<<<<<< 319d7692e1ff251b3ab1d38ff9c75f7bf7c00491
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.beans.*;
import java.io.Serializable;

/**
 *
 * @author Samuel
 */
public class adsdas implements Serializable {
    
    /**
	 * Onko tarpeellinen? Valitti, jos ei ollut.
	 */
	private static final long serialVersionUID = 1L;

	public static final String PROP_SAMPLE_PROPERTY = "sampleProperty";
    
    private String sampleProperty;
    
    private PropertyChangeSupport propertySupport;
    
    public adsdas() {
        propertySupport = new PropertyChangeSupport(this);
    }
    
    public String getSampleProperty() {
        return sampleProperty;
    }
    
    public void setSampleProperty(String value) {
        String oldValue = sampleProperty;
        sampleProperty = value;
        propertySupport.firePropertyChange(PROP_SAMPLE_PROPERTY, oldValue, sampleProperty);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
}
=======
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.beans.*;
import java.io.Serializable;

/**
 *
 * @author Samuel
 */
public class adsdas implements Serializable {
    
    /**
	 * Onko tarpeellinen? Valitti, jos ei ollut.
	 */
	private static final long serialVersionUID = 1L;

	public static final String PROP_SAMPLE_PROPERTY = "sampleProperty";
    
    private String sampleProperty;
    
    private PropertyChangeSupport propertySupport;
    
    public adsdas() {
        propertySupport = new PropertyChangeSupport(this);
    }
    
    public String getSampleProperty() {
        return sampleProperty;
    }
    
    public void setSampleProperty(String value) {
        String oldValue = sampleProperty;
        sampleProperty = value;
        propertySupport.firePropertyChange(PROP_SAMPLE_PROPERTY, oldValue, sampleProperty);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
}
>>>>>>> Juttuja
