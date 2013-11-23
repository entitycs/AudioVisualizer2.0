package application.setting;


	public class VisualizerDimension{
		double width;
		double height;
		
		public VisualizerDimension(){
			useExisting();
		}
		
		VisualizerDimension(double width, double height){
			this.width = width; this.height = height;
			
			if (this.width < 30 || this.height < 30)
				useExisting();
		}
		
		private void useExisting(){
			this.width = 500;
			this.height = 400;
			
//			 check file...?
		}
		
		public double width(){ return this.width; }
		public double height(){ return this.height; }

		public void setWidth(double width2)
		{
			this.width = width2;
		}

		public void setHeight(double height2)
		{
			this.height = height2;
		}
	}

