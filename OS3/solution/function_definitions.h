
fakeapplications.h:void construct_fake_applications(FreePacketDescriptorStore);

BoundedBuffer.h:BoundedBuffer createBB(Length);
BoundedBuffer.h:void          destroyBB(BoundedBuffer);
BoundedBuffer.h:void          blockingWriteBB(BoundedBuffer, BufferedItem);
BoundedBuffer.h:BufferedItem  blockingReadBB(BoundedBuffer);
BoundedBuffer.h:int     nonblockingWriteBB(BoundedBuffer, BufferedItem);
BoundedBuffer.h:int     nonblockingReadBB(BoundedBuffer, BufferedItem *);
BoundedBuffer.h:void          mapBB(BoundedBuffer, void (*mapFunction)(BufferedItem));


generic_queue.h:GQueue *create_gqueue (void); 
generic_queue.h:int destroy_gqueue(GQueue *gq);
generic_queue.h:int gqueue_enqueue (GQueue *gq, GQueueElement gqe);
generic_queue.h:int gqueue_dequeue (GQueue *gq, GQueueElement *gqep);
generic_queue.h:unsigned long gqueue_length(GQueue *gq);
generic_queue.h:void gqueue_purge(GQueue *gq);
generic_queue.h:void gqueue_map(GQueue *gq, void (*fp)(GQueueElement gqe));
generic_queue.h:void gqueue_purge_carefully (GQueue *gq, void (*cleaner)(GQueueElement gqe));

freepacketdescriptorstore.h:void blocking_get_pd(FreePacketDescriptorStore, PacketDescriptor *);
freepacketdescriptorstore.h:int  nonblocking_get_pd(FreePacketDescriptorStore, PacketDescriptor *);
freepacketdescriptorstore.h:void blocking_put_pd(FreePacketDescriptorStore, PacketDescriptor);
freepacketdescriptorstore.h:int  nonblocking_put_pd(FreePacketDescriptorStore, PacketDescriptor);

freepacketdescriptorstore__full.h:FreePacketDescriptorStore create_fpds(void);
freepacketdescriptorstore__full.h:void destroy_fpds(FreePacketDescriptorStore);

networkdevice.h:int send_packet(NetworkDevice, PacketDescriptor);
networkdevice.h:void register_receiving_packetdescriptor(NetworkDevice, PacketDescriptor*);
networkdevice.h:void await_incoming_packet(NetworkDevice);

networkdevice__full.h:NetworkDevice construct_network_device(void);

networkdriver.h:void blocking_send_packet(PacketDescriptor);
networkdriver.h:int  nonblocking_send_packet(PacketDescriptor);
networkdriver.h:void blocking_get_packet(PacketDescriptor*, PID);
networkdriver.h:int  nonblocking_get_packet(PacketDescriptor*, PID);
networkdriver.h:void init_network_driver(NetworkDevice nd, void *mem_start, unsigned long mem_length, FreePacketDescriptorStore * fpds_ptr);

*packetdescriptor.h:void init_packet_descriptor(PacketDescriptor *);
*packetdescriptor.h:void packet_descriptor_set_pid(PacketDescriptor *, PID);
*packetdescriptor.h:void packet_descriptor_set_destination(PacketDescriptor *, Destination);
*packetdescriptor.h:PID packet_descriptor_get_pid(PacketDescriptor *);
*packetdescriptor.h:Destination packet_descriptor_get_destination(PacketDescriptor *);

*packetdescriptorcreator.h:void create_free_packet_descriptors(FreePacketDescriptorStore fpds, void *mem_start, unsigned long mem_length);
