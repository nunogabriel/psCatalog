import { IOrders } from 'app/shared/model//orders.model';

export interface IOrderStatus {
    id?: number;
    orderStatusDescription?: string;
    orders?: IOrders[];
}

export class OrderStatus implements IOrderStatus {
    constructor(public id?: number, public orderStatusDescription?: string, public orders?: IOrders[]) {}
}
