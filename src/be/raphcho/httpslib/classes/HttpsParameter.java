package be.raphcho.httpslib.classes;


public class HttpsParameter 
{
	private String name;
	private Object Value;
	private String extraName;
	
	
	public HttpsParameter()
	{
		this(null,null);
	}
	public HttpsParameter(String name, Object value, String extraName)
    {
        setName(name);
        setValue(value);
        setExtraName(extraName);
    }
	public HttpsParameter(String name, Object value)
	{
		this(name,value, null);
	}
	
	
	
	
	
	@Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((Value == null) ? 0 : Value.hashCode());
	    result = prime * result + ((extraName == null) ? 0 : extraName.hashCode());
	    result = prime * result + ((name == null) ? 0 : name.hashCode());
	    return result;
    }
	@Override
    public boolean equals(Object obj) {
	    if (this == obj)
		    return true;
	    if (obj == null)
		    return false;
	    if (getClass() != obj.getClass())
		    return false;
	    HttpsParameter other = (HttpsParameter) obj;
	    if (Value == null) {
		    if (other.Value != null)
			    return false;
	    }
	    else if (!Value.equals(other.Value))
		    return false;
	    if (extraName == null) {
		    if (other.extraName != null)
			    return false;
	    }
	    else if (!extraName.equals(other.extraName))
		    return false;
	    if (name == null) {
		    if (other.name != null)
			    return false;
	    }
	    else if (!name.equals(other.name))
		    return false;
	    return true;
    }
	@Override
    public String toString() {
	    return "HttpsParameter [name=" + name + ", Value=" + Value + ", extraName=" + extraName + "]";
    }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return Value;
	}
	public void setValue(Object value) {
		Value = value;
	}
    public String getExtraName() {
        return extraName;
    }
    public void setExtraName(String extraName) {
        this.extraName = extraName;
    }
    

}
