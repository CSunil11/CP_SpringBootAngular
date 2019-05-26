export class BrandUserRequest {
	
	 public id : number;

     public name : string;
    
	 public brand : any[] = [];

	 public store : any;

	 public userLogin : any;
	 
	 public phone : any;
    
     constructor(data : any) {
        
        this.name = data.name;
        this.brand = data.brand;
        this.store = data.store;
        this.userLogin = data.userLogin;
        this.phone = data.phone;
    }
}