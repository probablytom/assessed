F1: FILE: packetdescriptorcreator_Vx.c
void create_free_packet_descriptors(FreePacketDescriptorStore fpds, void *mem_start, unsigned long mem_length);

F2: FILE: freepacketdescriptorstore_blocking_get_Vx.c:
void blocking_get_pd(FreePacketDescriptorStore, PacketDescriptor *);
F3: FILE: freepacketdescriptorstore_nonblocking_get_Vx.c:
int  nonblocking_get_pd(FreePacketDescriptorStore, PacketDescriptor *);
F4: FILE: freepacketdescriptorstore_Vx.c:
static void put_pd(FreePacketDescriptorStore fpds, PacketDescriptor pd);

F5: FILE: networkdevice_ctor_Vx.c
NetworkDevice construct_network_device(void)
F6: FILE: networkdevice_reg_rx_pd_Vx.c
void register_receiving_packetdescriptor(NetworkDevice, PacketDescriptor*);
F7: FILE: networkdevice_await_incoming_Vx.c
void await_incoming_packet(NetworkDevice);

---
F8:FILE: networkdriver_tx_Vx.c
networkdriver.h:void *sender_fn (void *args)
F9: FILE: networkdriver_rx_Vx.c
networkdriver.h:void *receiver_fn (void *args)
F10:FILE: networkdriver_Vx.c
void init_network_driver(NetworkDevice nd, void *mem_start, unsigned long mem_length, FreePacketDescriptorStore * fpds_ptr);

F11:FILE: packetdescriptor_Vx.c
void init_packet_descriptor(PacketDescriptor *);

F12:FILE: BoundedBuffer_Vx.c
void blockingWriteBB(BoundedBuffer, BufferedItem);
