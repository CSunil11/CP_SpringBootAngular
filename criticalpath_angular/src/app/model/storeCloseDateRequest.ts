export class StoreCloseDateRequest {
    
     public name : String;
    
     public description : String;
    
     public closeDate : String;
    
	 public stores : any[] = [];

	 public isActive : boolean;
    
     constructor(data : any) {
        
        this.name = data.name;
        this.description = data.description;
        this.closeDate = data.closeDate;
        this.stores = data.stores;
        this.isActive = data.isActive;
    }
}