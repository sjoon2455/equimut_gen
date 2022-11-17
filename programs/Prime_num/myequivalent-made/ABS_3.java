public class Prime_num {
	public static int primenum(String args[]) 
{ 
	int m,i,k,h=0,leap=1; 
	for(m=1;m<=5;m++)
	{ 
		k=(int)Math.sqrt(m+1);
		for(i=2;i<=Math.abs(k);i++) 
		{    
			if(m%i==0) 
			{
				leap=0;
				break;
			}
		}
		if(leap!=0) 
		{
			h++; 
		} 
		leap=1; 
	} 
	return h;
}
}
