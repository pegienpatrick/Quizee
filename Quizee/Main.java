
class people{
	private double regular_fare;
	public abstract String ticket();
	people()
	{
		this.regular_fare=3.0;
	}

	public double getFare(){
		return this.regular_fare;
	}



}

class adult extends people{
	adult()
	{
		super();
	}

	@override
	public double getFare(){
		return super.getFare();
	}

	@override

	public String ticket(){
		String tmp="Fare: "+getFare();
		return tmp;
	}





}



class student extends people{


	student(){
		super()
	}


	@override
	public double getFare(){
		return super.getFare()*0.5;
	}

	@override

	public String ticket(){
		String tmp="Fare: "+getFare();
		return tmp;
	}

}



public class Main{
	public static void main(String[] args){
		people Ed=new student();
		people Amy=new adult();

		System.out.println(Ed.ticket());//  prints "Fare: 1.5"
		System.out.println(Amy.ticket());// prints "Fare: 3.0"
	}
}