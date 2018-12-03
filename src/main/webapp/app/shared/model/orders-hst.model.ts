import { Moment } from 'moment';

export interface IOrdersHst {
    id?: number;
    orderReference?: string;
    customerId?: number;
    orderStatusCode?: number;
    orderDate?: Moment;
    billingAddress?: number;
    deliveryAddress?: number;
    deliveryDate?: Moment;
    createdBy?: string;
    createdDate?: Moment;
    lastModifiedBy?: string;
    lastModifiedDate?: Moment;
    ordersOrderReference?: string;
    ordersId?: number;
}

export class OrdersHst implements IOrdersHst {
    constructor(
        public id?: number,
        public orderReference?: string,
        public customerId?: number,
        public orderStatusCode?: number,
        public orderDate?: Moment,
        public billingAddress?: number,
        public deliveryAddress?: number,
        public deliveryDate?: Moment,
        public createdBy?: string,
        public createdDate?: Moment,
        public lastModifiedBy?: string,
        public lastModifiedDate?: Moment,
        public ordersOrderReference?: string,
        public ordersId?: number
    ) {}
}
