public class MyStringBuffer {

	char[] str;
	int numberOfChars;
	
	MyStringBuffer(int limit){
	     str = new char[limit];	
	
	}

	MyStringBuffer insert(int offset, char ch){
		if(numberOfChars != (str.length)){
			
			for(int i = numberOfChars-1; i >= offset; i--){
				str[i+1] = str[i];		
			}
			numberOfChars++;
			str[offset] = ch;
			
			
		}
		return this;
		
	}

	MyStringBuffer deleteChar(int offset){
		if(offset != 0){
			for(int i = offset; i < numberOfChars; i++){
				str[i-1] = str[i];		
			}
		numberOfChars--;		
		}
		return this;
		
	}

	void setCharAt(int offset, char ch){
		if((offset >= numberOfChars) && (numberOfChars != (str.length))){
				numberOfChars++;			
			}
		str[offset] = ch;
		
	}

	int length(){
		return numberOfChars;	
	}

	public String toString(){
	return new String(str, 0, numberOfChars);
	}

}

