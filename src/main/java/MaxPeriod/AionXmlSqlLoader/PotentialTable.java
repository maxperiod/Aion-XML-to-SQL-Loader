package MaxPeriod.AionXmlSqlLoader;

public class PotentialTable {
	public String tableName;	
	public String parentElement;
	public int level;
	public String elementName;
	
	
	
	public PotentialTable(String tableName, String parentElement, int level, String elementName) {		
		this.tableName = tableName;
		this.parentElement = parentElement;
		this.level = level;
		this.elementName = elementName;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((elementName == null) ? 0 : elementName.hashCode());
		result = prime * result + level;
		result = prime * result + ((parentElement == null) ? 0 : parentElement.hashCode());
		result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
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
		PotentialTable other = (PotentialTable) obj;
		if (elementName == null) {
			if (other.elementName != null)
				return false;
		} else if (!elementName.equals(other.elementName))
			return false;
		if (level != other.level)
			return false;
		if (parentElement == null) {
			if (other.parentElement != null)
				return false;
		} else if (!parentElement.equals(other.parentElement))
			return false;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[tableName: ");
		sb.append(tableName);
		sb.append(',');
		sb.append("parentElement: ");
		sb.append(parentElement);
		sb.append(',');
		sb.append("level: ");
		sb.append(level);
		sb.append(',');
		sb.append("elementName: ");
		sb.append(elementName);
		sb.append(']');
		
		return sb.toString();
		
	}
}
