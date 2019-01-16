import { Moment } from 'moment';
import { IOrderDet } from 'app/shared/model//order-det.model';

export interface ICustomerOrders {
    id?: number;
    orderReference?: string;
    orderDate?: Moment;
    deliveryDate?: Moment;
    createdBy?: string;
    createdDate?: Moment;
    lastModifiedBy?: string;
    lastModifiedDate?: Moment;
    orderDets?: IOrderDet[];
    orderStatusOrderStatusDescription?: string;
    orderStatusId?: number;
    addressAddressReference?: string;
    addressId?: number;
    customerCustomerName?: string;
    customerId?: number;
    deliveryAddressAddressReference?: string;
    deliveryAddressId?: number;
}

export class CustomerOrders implements ICustomerOrders {
    constructor(
        public id?: number,
        public orderReference?: string,
        public orderDate?: Moment,
        public deliveryDate?: Moment,
        public createdBy?: string,
        public createdDate?: Moment,
        public lastModifiedBy?: string,
        public lastModifiedDate?: Moment,
        public orderDets?: IOrderDet[],
        public orderStatusOrderStatusDescription?: string,
        public orderStatusId?: number,
        public addressAddressReference?: string,
        public addressId?: number,
        public customerCustomerName?: string,
        public customerId?: number,
        public deliveryAddressAddressReference?: string,
        public deliveryAddressId?: number
    ) {}
}
