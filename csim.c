#include "cachelab.h"
#include <stdlib.h>
#include <stdio.h>
#include <getopt.h>
#include <math.h>

typedef struct {
	int valid;
	int tag;
	int age; 
} Cash_line;

typedef struct {
	Cash_line *lines;
} Cash_set;

typedef struct  {
	Cash_set *sets;
} Cash;

int no_of_misses;
int no_of_hits;
int no_of_evicts;
int set_index_bits;
int block_size_bit;
int Associtivity; //no of lines per Cash_set
int v;
char *filename;
Cash mycache;

Cash initialize_Cache(Cash c, int set_index_bits, int Associtivity){
	Cash_set myset;
	int number_of_sets;
	number_of_sets = (int) pow(2.0, set_index_bits);
	c.sets = (Cash_set *) malloc(number_of_sets*sizeof(Cash_set));
	int i; 
	int j;
	Cash_line myline;
	
	for (i = 0; i < number_of_sets; i+=1) {
		myset.lines = (Cash_line *) malloc(sizeof(Cash_line) * Associtivity);
		c.sets[i] = myset;
		
		for (j = 0; j < Associtivity; j+=1) {
			myline.valid = 0;
			myline.age = 0;
			myline.tag = 0;
			myset.lines[j] = myline;		
		}

	}	
	return c;
}

int find_emptyline(Cash_set mycacheset, int Associtivity) {
	Cash_line t;
	int n;
	for(n = 0; n < Associtivity; n+=1) {
		t = mycacheset.lines[n];
		if(t.valid == 0) 
			return 1; // F0ND
	} 
	return -1;
}

int get_emptylineindex(Cash_set mycacheset, int Associtivity) {
	Cash_line t;
	int n;
	for(n = 0; n < Associtivity; n+=1) {
		t = mycacheset.lines[n];
		if(t.valid == 0) 
			return n;
	} 
	return 0;
}

void update_age(Cash mycache, int set_index_bits, int Associtivity){
	int i;
	int j;
	int up = (int) pow(2.0, set_index_bits);
	for (i = 0; i < up; i+=1) {
		for (j = 0; j < Associtivity; j+=1) {
			mycache.sets[i].lines[j].age += 1;
		}
	}
}

void update_age2(Cash_set mycacheset, int Associtivity){
	int j;
	for (j = 0; j < Associtivity; j+=1) {
			mycacheset.lines[j].age += 1;
		}
}

int get_max_age_index(Cash_set kl, int Associtivity) {
	int min_index = 0;
	int max_value= kl.lines[0].age;
	//int min_index_of_max_value = 0;
	int i;
	for(i = 0 ; i < Associtivity; i+=1) {
		if(max_value < kl.lines[i].age) {
			max_value = kl.lines[i].age;
			min_index = i;
		} 
	}
	return min_index;
}

void simulate_cache(Cash mycache, int set_index_bits, int Associtivity, int  block_size_bit, int hexadress) {
	printf("*****\n");
	unsigned int x; //
	unsigned int set_index;
	int tag_size;
	int tag_bits;
	tag_size = 32- (set_index_bits + block_size_bit);
	//printf("tag_size=%d\n", tag_size);
	x = hexadress << (tag_size);
	//printf("x=%d\n", x );
	set_index = x  >> (block_size_bit + tag_size);
	//printf("set_index=%d\n", set_index);
	tag_bits = hexadress >> (set_index_bits + block_size_bit);
 	//printf("hi\n");
 	Cash_set kl = mycache.sets[set_index]; 
 	//printf("bye\n");
 	int p;
 	//printf("%d\n", Associtivity);
 	for(p = 0; p < Associtivity; p+=1) {
 		if(kl.lines[p].valid == 1) {	
 			if(kl.lines[p].tag == tag_bits) {
 				no_of_hits++; 
 				printf("HIT++\n");
	 			kl.lines[p].age = 0;
	 			update_age(mycache, set_index_bits, Associtivity);
 				//update_age2(kl, Associtivity);
 				return;
 			}
 		}
 	}
 	//printf("%d\n", no_of_hits);
 	//printf("I am here\n");
 	
	printf("MISS++\n");	
 	
 		 // There is And empty line available and we need to get that line. 
 		if (1==find_emptyline(kl, Associtivity)) {
 			//printf("MISS and HIT");
 			no_of_misses+=1;
 			int i;
 			i = get_emptylineindex(kl, Associtivity);  // Copying my tag to the new line.
 			kl.lines[i].tag = tag_bits;
 			kl.lines[i].valid = 1;
 			kl.lines[i].age = 0;
 			update_age(mycache, set_index_bits, Associtivity);
 			//update_age2(kl, Associtivity);
 			return;
 		} else if (-1 ==find_emptyline(kl, Associtivity)) { // No free line found, need to evict the oldest one.
 			printf("evict++\n");
 			no_of_misses+=1;
 			no_of_evicts+=1;
 			int max_age_index;
 			max_age_index = get_max_age_index(kl, Associtivity);
 			kl.lines[max_age_index].tag = tag_bits;
 			kl.lines[max_age_index].valid = 1;
 			kl.lines[max_age_index].age = 0;
 			update_age(mycache, set_index_bits, Associtivity);
 			//update_age2(kl, Associtivity);
 			return;
 		}
}


int main(int argc, char **argv) {	
	// reading the input part.
	char input;
	while((input = getopt(argc, argv, "s:E:b:t:")) != -1) {
		switch(input){
			case 's':
				set_index_bits = atoi(optarg);
				break;
			case 'E':
				Associtivity = atoi(optarg);
				break;
			case 'b':
				block_size_bit = atoi(optarg);
				break;
			case 't':
				filename = optarg;
				//printf("%s\n", filename);
				break;	
			default:
				break;				
		}
	}
	//Now we initialize cache
	//printf("THIS IS MY FILE\n");
	mycache = initialize_Cache(mycache, set_index_bits, Associtivity);
	/*
	int j;
	int u;
	for (j=0; j<16;j++) {
		printf("I am alive\n");
		Cash_set s = mycache.sets[j];
		for(u=0; u < Associtivity; u++) {
			Cash_line l = s.lines[0];
			printf("****");
			printf("%ld\n", l.tag);
			printf("%d\n", l.valid);
		}
	}
	*/
	//Cache is initialized. Now I need to simulate it.
	//And to do so I need to read the trace
	int hexadress;
	int d;
	char c;
    FILE *reading;
    reading = fopen(filename, "r");
    while (!feof(reading)) {
    	if (fscanf(reading, "%c %x,%d\n", &c, &hexadress, &d)==3) {
	   
	    	switch(c) {
	    		case 'I':
	    			break;
	    		case 'S':
	    		//printf("S\n");
	    			simulate_cache(mycache, set_index_bits, Associtivity, block_size_bit, hexadress);
	    			break;
	    		case 'L':
	    		//printf("L\n");
	    			simulate_cache(mycache, set_index_bits, Associtivity, block_size_bit, hexadress);
	    			break;
	    		case 'M':
	    			simulate_cache(mycache, set_index_bits, Associtivity, block_size_bit, hexadress);
	    			simulate_cache(mycache, set_index_bits, Associtivity, block_size_bit, hexadress);
	    			break;
	    		default:
	    			break;			
	    	}
	    }
    }


	printSummary(no_of_hits, no_of_misses, no_of_evicts);
	
	return 0;
}
