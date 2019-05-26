export class NotificationRequest {
	
	 public id : any;

     public name : string;
    
	 public description : string

	 public subject : string;

	 public body : string;
	 
	 public userGroups : string;

	 public triggerEvent : any;

	 public delayTime : any;

	 public reminderTime : any;

	 public reminderCount : any;

	 public notificationType : any;
    
     constructor(data : any) {
        
    	 this.id = data.id;
        this.name = data.name;
        this.description = data.description;
        this.subject = data.subject;
        this.body = data.body;
        this.userGroups = data.userGroups;
        this.triggerEvent = data.triggerEvent;
        this.delayTime = data.delayTime;
        this.reminderTime = data.reminderTime;
        this.reminderCount = data.reminderCount;
        this.notificationType = data.notificationType;
    }
}